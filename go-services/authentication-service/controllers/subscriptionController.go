package controllers

import (
	"bytes"
	"encoding/gob"
	"fmt"
	"net/http"
	"score-tracker/authentication-service/initializers"
	"score-tracker/authentication-service/models"
	"score-tracker/caas"
	"strconv"

	"github.com/gin-gonic/gin"
)

func RegisterSubscription(c *gin.Context) {
	var body struct {
		TeamId string
	}

	if c.Bind(&body) != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Failed to read body",
		})
		return
	}
	user, _ := c.Get("user")
	subsciptionInfo := models.SubscriptionInfo{UserID: user.(models.User).ID, TeamID: body.TeamId}
	result := initializers.DB.Create(&subsciptionInfo)

	if result.Error != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Failed to Subscribe for user",
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{})
}

func GetSubscription(c *gin.Context) {

	user, _ := c.Get("user")
	cache, _ := c.Get("cache")
	userId := strconv.FormatUint(uint64(user.(models.User).ID), 10)
	var cacheValue = cache.(caas.Cache).Get(userId)
	var subInfo []models.SubscriptionInfo
	if cacheValue == nil {
		initializers.DB.Find(&subInfo, "User_ID = ? ", userId)
		var byteBuffer bytes.Buffer

		encoder := gob.NewEncoder(&byteBuffer)
		for _, item := range subInfo {
			err := encoder.Encode(item)
			if err != nil {
				fmt.Println("Error: ", err)
				break
			}
		}
		byteArray := byteBuffer.Bytes()
		cache.(caas.Cache).Set(userId, byteArray)
	} else {
		fmt.Println("In cache")
		decoder := gob.NewDecoder(bytes.NewReader(cacheValue))
		for {
			var item models.SubscriptionInfo
			err := decoder.Decode(&item)
			if err != nil {
				break // Exit the loop when there's an error (likely EOF)
			}
			subInfo = append(subInfo, item)
		}
	}

	c.JSON(http.StatusOK, gin.H{"subscriptionList": subInfo})
}

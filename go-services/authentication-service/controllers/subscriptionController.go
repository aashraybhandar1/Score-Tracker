package controllers

import (
	"net/http"
	"score-tracker/authentication-service/initializers"
	"score-tracker/authentication-service/models"

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

	var subInfo []models.SubscriptionInfo
	initializers.DB.Find(&subInfo, "User_ID = ? ", user.(models.User).ID)

	c.JSON(http.StatusOK, gin.H{"subscriptionList": subInfo})
}

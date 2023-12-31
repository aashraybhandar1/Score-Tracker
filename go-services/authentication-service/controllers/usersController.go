package controllers

import (
	"fmt"
	"net/http"
	"os"
	"score-tracker/authentication-service/initializers"
	"score-tracker/authentication-service/models"
	"score-tracker/notification-service/handlers"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"
	"golang.org/x/crypto/bcrypt"
)

var SSE = handlers.NewServer()

func Signup(c *gin.Context) {
	//Get email/pass from req.body

	var body struct {
		Email    string
		Password string
	}

	if c.Bind(&body) != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Failed to read body",
		})
		return
	}
	//Hash the password
	hash, err := bcrypt.GenerateFromPassword([]byte(body.Password), 10)

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Failed to has password",
		})
		return
	}

	//Create the User

	user := models.User{Email: body.Email, Password: string(hash)}
	result := initializers.DB.Create(&user)

	if result.Error != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Failed to create User",
		})
		return
	}

	//Respond
	c.JSON(http.StatusOK, gin.H{})
}

func Login(c *gin.Context) {
	// Get email and pass of req body
	var body struct {
		Email    string
		Password string
	}

	if c.Bind(&body) != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Failed to read body",
		})
		return
	}
	//Lookup requested user
	var user models.User

	initializers.DB.First(&user, "email = ?", body.Email)

	if user.ID == 0 {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "invalid user or password",
		})
		return
	}

	//Compare sent in pass with saved user pass hash
	err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(body.Password))

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "invalid user or password",
		})
		return
	}

	//Generate a jwt token
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"sub": user.ID,
		"exp": time.Now().Add(time.Hour * 24 * 365).Unix(),
	})

	tokenString, err := token.SignedString([]byte(os.Getenv("SECRET")))

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Failed to create token",
		})
		return
	}

	//Send it back
	c.SetSameSite(http.SameSiteLaxMode)
	c.SetCookie("Authorization", tokenString, 3600*24*365, "", "", false, true)
	c.JSON(http.StatusOK, gin.H{})
}

func Validate(c *gin.Context) {
	user, _ := c.Get("user")

	c.JSON(http.StatusOK, gin.H{
		"message": user.(models.User).Email + " is logged in",
	})
}

func Register(c *gin.Context) {
	c.Header("Content-Type", "text/event-stream")
	c.Header("Cache-Control", "no-cache")
	c.Header("Connection", "keep-alive")

	updateChan := make(chan string)
	defer close(updateChan)

	// Start a goroutine to send updates to the client
	/*go func() {
		for {
			update := time.Now().Format("2006-01-02 15:04:05")
			updateChan <- update
			time.Sleep(1 * time.Second) // Send updates every 1 second
		}
	}()*/

	SSE.Broadcaster.NewConnection <- updateChan
	// Loop to write updates to the client as they arrive
	for {
		select {
		case update := <-updateChan:
			// Write SSE data to the connection
			c.SSEvent("message", update)
			c.Writer.Flush()
		case <-c.Writer.CloseNotify():
			// Client closed the connection
			fmt.Println("Client disconnected")
			return
		}
	}
}

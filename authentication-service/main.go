package main

import (
	"authentication-service/controllers"

	"authentication-service/initializers"

	"github.com/gin-gonic/gin"
)

// Runs before main the init function
func init() {
	initializers.LoadEnvVariables()
	initializers.ConnectToDB()
	initializers.SyncDatabse()
}

func main() {
	r := gin.Default()

	r.POST("/signup", controllers.Signup)
	r.POST("login", controllers.Login)

	r.Run()
}

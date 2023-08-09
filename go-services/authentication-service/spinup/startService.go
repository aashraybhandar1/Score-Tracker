package spinup

import (
	"score-tracker/authentication-service/controllers"
	"score-tracker/authentication-service/middleware"

	"github.com/gin-gonic/gin"
)

func StartService() {
	r := gin.Default()

	r.POST("/signup", controllers.Signup)
	r.POST("/login", controllers.Login)
	r.GET("/registerForUpdates", controllers.Register)
	r.GET("/validate", middleware.RequireAuth, controllers.Validate)
	r.POST("/subscribtion", middleware.RequireAuth, controllers.RegisterSubscription)
	r.GET("/subscribtion", middleware.RequireAuth, controllers.GetSubscription)
	r.Run()
}

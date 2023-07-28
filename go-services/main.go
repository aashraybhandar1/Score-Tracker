package main

import (
	"score-tracker/authentication-service/initializers"
	"score-tracker/authentication-service/spinup"
	"score-tracker/notification-service/cronjobs"
)

// Runs before main the init function
func init() {
	initializers.LoadEnvVariables()
	initializers.ConnectToDB()
	initializers.SyncDatabse()
	initializers.RegisterService()
	cronjobs.GetLiveScore()
}

func main() {
	spinup.StartService()
}

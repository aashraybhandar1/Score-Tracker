package main

import (
	"score-tracker/authentication-service/initializers"
	"score-tracker/authentication-service/spinup"
	"score-tracker/notification-service/cronjobs"
	"score-tracker/notification-service/kafkaInit"
)

// Runs before main the init function
func init() {
	initializers.LoadEnvVariables()
	initializers.ConnectToDB()
	initializers.SyncDatabse()
	initializers.RegisterService()
	kafkaInit.ConnectToKafka()
	cronjobs.GetLiveScore()
}

func main() {
	spinup.StartService()
}

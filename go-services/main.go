package main

import (
	"score-tracker/authentication-service/initializers"
	"score-tracker/authentication-service/spinup"
)

// Runs before main the init function
func init() {
	initializers.LoadEnvVariables()
	initializers.ConnectToDB()
	initializers.SyncDatabse()
	initializers.RegisterService()
	//cronjobs.GetLiveScore()
}

func main() {
	//go producer.ProduceMessages()
	//go consumer.ConsumeMessages()
	spinup.StartService()
}

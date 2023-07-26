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
}

func main() {
	spinup.StartService()
}

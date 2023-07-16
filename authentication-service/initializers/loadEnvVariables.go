package initializers

import (
	"log"

	"github.com/joho/godotenv"
)

// Capital Letter functions can be used in other packages
func LoadEnvVariables() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}
}

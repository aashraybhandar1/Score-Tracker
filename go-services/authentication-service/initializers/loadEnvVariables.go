package initializers

import (
	"fmt"
	"log"
	"path/filepath"
	"runtime"

	"github.com/joho/godotenv"
)

func LoadEnvVariables() {
	_, filename, _, _ := runtime.Caller(0)
	fmt.Println(filename)
	err := godotenv.Load(filepath.Join(filename, "../../.env"))
	if err != nil {
		log.Fatal("Error loading .env file")
	}
}

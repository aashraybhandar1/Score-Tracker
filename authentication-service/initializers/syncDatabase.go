package initializers

import "authentication-service/models"

func SyncDatabse() {
	DB.AutoMigrate(&models.User{})
}

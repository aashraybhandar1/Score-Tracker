package initializers

import "score-tracker/authentication-service/models"

func SyncDatabse() {
	DB.AutoMigrate(&models.User{})
	DB.AutoMigrate(&models.SubscriptionInfo{})
}

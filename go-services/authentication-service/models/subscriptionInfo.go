package models

import "gorm.io/gorm"

type SubscriptionInfo struct {
	gorm.Model
	UserID uint
	TeamID string
}

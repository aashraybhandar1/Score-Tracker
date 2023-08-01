package processor

import (
	"fmt"
	"score-tracker/authentication-service/models"
)

func GetNewEvents(liveScores models.LiveFixture, data map[int]int) {
	for _, item := range liveScores.Response {
		if _, ok := data[item.CurrentFixture.Id]; !ok {
			data[item.CurrentFixture.Id] = 0
		}
		if data[item.CurrentFixture.Id] < len(item.Events) {
			for i := data[item.CurrentFixture.Id]; i < len(item.Events); i++ {
				event := models.Events{
					FixtureID:    item.CurrentFixture.Id,
					CurrentScore: item.CurrentScore,
					TeamsPlaying: item.TeamsPlaying,
					CurrentEvent: item.Events[i],
				}
				fmt.Println(event)
			}
			data[item.CurrentFixture.Id] = len(item.Events)
		}
	}
}

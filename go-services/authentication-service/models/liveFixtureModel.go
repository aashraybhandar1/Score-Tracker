package models

import "time"

type LiveFixture struct {
	Response []struct {
		Fixture struct {
			Id      int       `json:"id"`
			Referee string    `json:"referee"`
			Date    time.Time `json:"date"`
			Status  struct {
				Short   string `json:"short"`
				Elapsed int    `json:"elapsed"`
			} `json:"status"`
		} `json:"fixture"`

		Teams struct {
			HomeId int `json:"home.id"`
			AwayId int `json:"away.id"`
		} `json:"teams"`

		Score struct {
			Home int `json:"home"`
			Away int `json:"away"`
		} `json:"goals"`

		Events []struct {
			Elapsed          int    `json:"time.elapsed"`
			Extra            int    `json:"time.extra"`
			TeamId           int    `json:"team.id"`
			PlayerId         int    `json:"player.id"`
			PlayerName       string `json:"player.name"`
			AssistPlayerID   int    `json:"assist.id"`
			AssistPlayerName string `json:"assist.name"`
			EventType        string `json:"type"`
			EventDetail      string `json:"detail"`
			Comments         string `json:"comments"`
		} `json:"events"`
	} `json:"response"`
	Get string `json:"get"`
}

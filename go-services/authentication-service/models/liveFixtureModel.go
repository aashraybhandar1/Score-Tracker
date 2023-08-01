package models

import "time"

type LiveFixture struct {
	Response []struct {
		CurrentFixture Fixture `json:"fixture"`
		TeamsPlaying   Teams   `json:"teams"`
		CurrentScore   Score   `json:"goals"`
		Events         []Event `json:"events"`
	} `json:"response"`
}

type Event struct {
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
}

type Score struct {
	Home int `json:"home"`
	Away int `json:"away"`
}

type Teams struct {
	HomeId int `json:"home.id"`
	AwayId int `json:"away.id"`
}

type Fixture struct {
	Id      int       `json:"id"`
	Referee string    `json:"referee"`
	Date    time.Time `json:"date"`
	Status  struct {
		Short   string `json:"short"`
		Elapsed int    `json:"elapsed"`
	} `json:"status"`
}

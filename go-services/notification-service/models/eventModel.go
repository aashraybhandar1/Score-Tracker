package models

type Events struct {
	FixtureID    int
	TeamsPlaying Teams
	CurrentScore Score
	CurrentEvent Event
}

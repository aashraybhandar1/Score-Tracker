package cronjobs

import (
	"fmt"
	"io"
	"net/http"
)

func GetLiveScore() {
	fmt.Println("IN HERE")
	url := "https://api-football-v1.p.rapidapi.com/v3/fixtures?live=all"

	req, _ := http.NewRequest("GET", url, nil)

	req.Header.Add("X-RapidAPI-Key", "")
	req.Header.Add("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")

	res, _ := http.DefaultClient.Do(req)

	defer res.Body.Close()
	body, _ := io.ReadAll(res.Body)

	fmt.Println(res)
	fmt.Println(string(body))
}

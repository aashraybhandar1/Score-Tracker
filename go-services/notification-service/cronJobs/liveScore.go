package cronjobs

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"score-tracker/authentication-service/models"
)

func GetLiveScore() {
	url := "https://api-football-v1.p.rapidapi.com/v3/fixtures?live=all"

	req, _ := http.NewRequest("GET", url, nil)

	req.Header.Add("X-RapidAPI-Key", os.Getenv("RAPID_API_KEY"))
	req.Header.Add("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")

	res, _ := http.DefaultClient.Do(req)
	body, _ := ioutil.ReadAll(res.Body)

	defer res.Body.Close()

	temp := models.LiveFixture{}

	err := json.Unmarshal([]byte(body), &temp)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println(temp.Response[0].Fixture)

}

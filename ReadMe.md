# GameHub — Entertainment Gaming Platform

A Spring MVC web application featuring two browser-based games (Tic-Tac-Toe and Rock Paper Scissors) with user authentication, per-user analytics, and game history tracking.

---

## Requirements

- **Java** 17
- **Maven** 3.8+
- **MongoDB** running locally on `localhost:27017`

---

## How to Run

1. Make sure MongoDB is running
2. Open a terminal in the project root folder
3. Run: `mvn spring-boot:run`
4. Open your browser and go to `http://localhost:8080`

---

## Pages

| Page                | URL            | Description                                                      |
| ------------------- | -------------- | ---------------------------------------------------------------- |
| Home                | `/`            | Landing page with game overview                                  |
| Register            | `/register`    | Create a new account                                             |
| Login               | `/login`       | Sign in to your account                                          |
| Dashboard           | `/dashboard`   | Game selection hub                                               |
| Tic-Tac-Toe         | `/games/ttt`   | Play against random AI                                           |
| Rock Paper Scissors | `/games/rps`   | Play against random AI                                           |
| Records             | `/records`     | View all logged game entries, filterable by game type and result |
| Log a Game          | `/records/new` | Manually submit a game result form                               |
| Profile             | `/profile`     | Your win/loss/draw stats per game                                |

> **Server Hit Counter** — a widget fixed to the bottom-right of every page showing total HTTP requests since the server started, updated every 3 seconds.

---

## Project Structure

src/
├── main/
│ ├── java/com/games/final_projest_java/
│ │ ├── config/ # Security and password encoder configuration
│ │ ├── controller/ # MVC controllers for each page and REST API
│ │ ├── dto/ # Form input objects with validation annotations
│ │ ├── model/ # MongoDB document models (User, GameRecord)
│ │ ├── repository/ # Spring Data MongoDB repositories
│ │ └── service/ # Business logic (UserService, GameRecordService, PageHitService)
│ └── resources/
│ ├── static/
│ │ ├── css/ # main.css, ttt.css, rps.css
│ │ └── js/ # hits.js, ttt.js, rps.js
│ ├── templates/
│ │ ├── auth/ # login.html, register.html
│ │ ├── games/ # tictactoe.html, rps.html
│ │ ├── records/ # list.html, form.html
│ │ └── user/ # profile.html
│ └── application.properties
└── test/
└── java/com/games/final_projest_java/
├── controller/ # AuthControllerTest, GameRecordControllerTest, PageHitControllerTest
└── service/ # UserServiceTest, GameRecordServiceTest, PageHitServiceTest, UserModelTest

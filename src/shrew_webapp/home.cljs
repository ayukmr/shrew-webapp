(ns shrew-webapp.home
  (:require [shrew-webapp.link :as link]
            [rum.core :as rum]))

(rum/defc view []
  [:<> [:header [:h1 "Shrew 🐁"]
                [:p "FRC scouting webapp for Team 4904."]]
       [:section [:h3 (link/view "/scout"     "Scout")]
                 [:h3 (link/view "/points"    "Points")]
                 [:h3 (link/view "/responses" "Responses")]
                 [:h3 (link/view "/settings"  "Settings")]]])

(ns shrew.scout.responses
  (:require [shrew.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]))

(defc view < rum/reactive [!settings team match]
  [:<> [:h1 "Responses"]
       [:form {:id "form"}
              (for [[idx question] (->> (rum/react !settings)
                                        (:questions)
                                        (map-indexed vector))]
                (let [question-id (str "question-" idx)]
                  [:<> {:key question-id}
                       [:label {:for question-id} question]
                       [:textarea {:id question-id :name question}]]))]])

(defn submit [!team !match]
  (request/post "/responses"
                {:scouting @!team
                 :match @!match
                 :responses
                 (->> (.getElementById js/document "form")
                      (js/FormData.)
                      (.fromEntries js/Object)
                      (.entries js/Object)
                      (map (fn [[question response]]
                             {:question question
                              :response response})))}
                (cookies/get "scout")))

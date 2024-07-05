(ns shrew.scout.responses
  (:require [shrew.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]))

(def !responses (atom {}))

(defn save-responses [type]
  (->> (.getElementById js/document "form")
       (js/FormData.)
       (.fromEntries js/Object)
       (.entries js/Object)
       (mapv (fn [[question response]]
              {:question question
               :response response}))
       (swap! !responses assoc type)))

(defc view < rum/reactive [!questions type]
  [:<> [:header  [:h1 "Responses"]]
       [:section [:form {:id "form"}
              (for [[idx question] (->> (get (rum/react !questions) type)
                                        (map-indexed vector))]
                (let [question-id (str "question-" idx)]
                  [:<> {:key question-id}
                       [:label {:for question-id} question]
                       [:textarea {:id question-id
                                   :name question
                                   :defaultValue (get-in @!responses [type idx :response])
                                   :on-change #(save-responses type)}]]))]]])

(defn submit [team match]
  (doseq [[type responses] @!responses]
    (request/post (str "/responses/" (subs (str type) 1))
                  {:scouting  team
                   :match     match
                   :responses (filter #(not-empty (get %1 :response)) responses)}
                  (or (cookies/get "scout") (cookies/get "admin"))))
  (reset! !responses {}))

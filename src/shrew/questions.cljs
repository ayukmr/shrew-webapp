(ns shrew.questions
  (:require [shrew.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]
            [clojure.string :as string]))

(def !questions (atom {}))
(def !type (atom :pre))
(def !question (atom ""))

(defn fetch []
  (request/get "/questions"
               (cookies/get "admin")
               #(reset! !questions %)))

(defn submit [event]
  (.preventDefault event)
  (doseq [[type questions] @!questions]
    (request/post (str "/questions/" (subs (str type) 1))
                  {:questions questions}
                  (cookies/get "admin"))))

(defc view < rum/reactive []
  [:<> [:header [[:h1 "Questions"]
                 [:p  "Questions to collect responses on."]]]
       [:section [:label {:for "type"} "Question Type"]
                 [:select {:id "type"
                           :on-change #(reset! !type (keyword (.. % -target -value)))}
                          (for [type #{:pre :post}
                                :let [str-type (subs (str type) 1)]]
                            [:option {:key str-type :value str-type}
                                     (string/capitalize str-type)])]]
       [:section [:label {:for "question"} "Add Question"]
                 [:div {:style {:display "flex"}}
                       [:input {:id "question"
                                :type "text"
                                :style {:display "inline" :margin-bottom "6px"}
                                :on-change #(reset! !question (.. % -target -value))}]
                       [:button {:style {:height "fit-content"
                                         :margin-left "7px"}
                                 :on-click #(when (not-empty @!question)
                                              (swap! !questions
                                                     update-in [@!type]
                                                     conj @!question)
                                              (->
                                                (.getElementById js/document "question")
                                                (.-value)
                                                (set! "")))}
                                "Add"]]
                 (for [[idx question]
                       (->> (get (rum/react !questions) (rum/react !type))
                            (map-indexed vector))]
                   [:p {:key (str "question-" idx)}
                       [:button {:style {:padding "0px 3px"
                                         :margin-right "7px"}
                                 :on-click (fn []
                                             (swap! !questions
                                                    update-in [@!type]
                                                    (fn [questions]
                                                      (keep-indexed #(if (not= %1 idx) %2) questions))))}
                                 "×"]
                       question])]
        [:section [:input {:value "Save"
                           :type "submit"
                           :on-click submit}]]])

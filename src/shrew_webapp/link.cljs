(ns shrew-webapp.link
  (:require [rum.core :refer [defc] :as rum]
            [secretary.core :as secretary]))

(defc view [path & children]
  [:a {:href path
       :on-click #(do (.preventDefault %)
                      (navigate path))}
      children])

(defn navigate [path]
  (.pushState js/history nil nil path)
  (secretary/dispatch! path))

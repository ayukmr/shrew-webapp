(ns shrew-webapp.auth
  (:require [shrew-webapp.link :as link]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]))

(defc view []
  [:<> [:header [:h1 "Authentication"]]
       [:section [:form {:id "form"}
                        [:label {:for "team"} "Team Number"]
                        [:input {:id  "team" :name "team" :type "number"}]
                        [:label {:for "scout"} "Scout Password"]
                        [:input {:id  "scout" :name "scout" :type "password"}]
                        [:label {:for "admin"} "Admin Password (optional)"]
                        [:input {:id  "admin" :name "admin" :type "password"}]
                        [:input {:value "Log In" :type "submit" :on-click submit}]]]])

(defn submit [event]
  (.preventDefault event)
  (let [data (->> (.getElementById js/document "form")
                  (js/FormData.)
                  (.fromEntries js/Object))
        team  (.-team  data)
        scout (.-scout data)
        admin (.-admin data)]
    (if (and (seq team) (seq scout))
      (do (cookies/set! "team"  team)
          (cookies/set! "scout" scout)
          (cookies/set! "admin" admin)
          (.back js/history))
      (js/alert "Input team number and scout password!"))))

(defn require [type callback]
  (if (seq (cookies/get type))
    (callback)
    (link/navigate "/auth")))

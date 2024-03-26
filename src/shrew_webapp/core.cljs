(ns shrew-webapp.core
  (:require [shrew-webapp.home :as home]
            [shrew-webapp.auth :as auth]
            [shrew-webapp.scout.core :as scout]
            [shrew-webapp.points :as points]
            [shrew-webapp.responses :as responses]
            [shrew-webapp.settings :as settings]
            [secretary.core :refer-macros [defroute] :as secretary]
            [rum.core :as rum]))

(enable-console-print!)

(defn render [view]
  (rum/mount view (.getElementById js/document "root")))

(defroute "/" []
  (render (home/view)))

(defroute "/auth" []
  (render (auth/view)))

(defroute "/scout" []
  (->> #(render (scout/view))
       (auth/require "scout")))

(defroute "/points" []
  (->> #(render (points/view))
       (auth/require "admin")))

(defroute "/responses" []
  (->> #(render (responses/view))
       (auth/require "admin")))

(defroute "/settings" []
  (->> #(render (settings/view))
       (auth/require "admin")))

(js/addEventListener "popstate"
                     #(secretary/dispatch! (.-pathname js/location)))

(secretary/dispatch! (.-pathname js/location))

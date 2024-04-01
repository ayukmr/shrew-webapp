(ns shrew.request
  (:require [reagent.cookies :as cookies]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn get [path auth callback]
  (go
    (let [{body :body}
          (<! (http/get (str "http://localhost:3000" path "/"
                             (cookies/get "team"))
                        (when (some? auth)
                          {:with-credentials? false
                           :headers {"Authorization" auth}})))]
      (callback body))))

(defn post [path body auth]
  (http/post (str "http://localhost:3000" path "/"
                  (cookies/get "team"))
             (merge {:json-params body}
                    (when (some? auth)
                      {:with-credentials? false
                       :headers {"Authorization" auth}}))))

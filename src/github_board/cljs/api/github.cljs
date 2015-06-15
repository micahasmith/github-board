(ns ^:figwheel-always github-board.core.cljs.api.github
  (:require
    [goog.net.XhrIo :as xhr]
    [cljs.core.async :as async :refer [chan close!]])
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]))

(defn- get-url []
  (str "https://api.github.com/"))

(defn login-test [username password]
  "returns a chan that will have :ok if valid credentials, :not-ok if not"
  (let [result-chan (chan 1)]
    (xhr/send
      ;; url
      (str (get-url))

      ;; callback
      (fn [e]
        (let [res (-> e .-target .getResponseJson)]
          (go
            ;; reply :ok if cool, :not-ok if not cool
            (>! result-chan (if (-> res .-current_user_url nil?) :not-ok :ok))

            ;; auto close, why not?
            (close! result-chan)
            )))

      ;; type
      "GET"

      ;; post content body
      nil

      ;; headers
      {
       "Authorization" (str "Basic " (js/btoa (str username ":" password)))
       "Accept"        "application/vnd.github.v3+json"
       })
    result-chan))



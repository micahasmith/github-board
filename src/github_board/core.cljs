(ns ^:figwheel-always github-board.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [github-board.core.cljs.views.login-view :as login-view]
            [github-board.core.cljs.respond :as agents]
            [cljs.core.async :as async :refer [chan close!]])
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom
                     {
                      ;;
                      ;; props about being logged in/ the user
                      :logged-in    false
                      :username     ""
                      :password     ""

                      ;;
                      ;; props about current view
                      :current-view :repos
                      :current-repo ""

                      ;;
                      ;; data
                      :repos        []
                      }))

(defn main []
  (let [req-chan (chan)
        pub-chan (chan)
        notif-chan (pub pub-chan :topic)]

    ;; serve loop
    (go
      (while true
        (agents/respond-to (<! req-chan))))


    (om/root
      (fn [{:keys [logged-in current-view current-repo] :as app-state} owner]
        (reify
          om/IRender
          (render [this]
            (if-not logged-in
              (om/build login-view/view this)
              (condp = current-view
                :repos ())
              )
            )))
      app-state
      {
       :shared {:req-chan   req-chan
                :notif-chan notif-chan
                :pub-chan   pub-chan}
       :target (. js/document (getElementById "app"))})))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )


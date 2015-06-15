(ns ^:figwheel-always github-board.core.cljs.views.login-view
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [github-board.core.cljs.util :as util]
            [cljs.core.async :as async :refer [<! put!]]
            [github-board.core.cljs.api.github :as github])
  (:require-macros
    [cljs.core.async.macros :refer [go]]))


(defn- onLoginClick [app-state owner {:keys [username password]}]
  (go
    (let [login-check (<! (github/login-test username password))]
      (condp = login-check
        :ok (do
              (.log js/console ":ok!")
              )
        :not-ok (do
                  (put! (-> (om/get-shared owner) :pub-chan) {:topic on-login })
                  (om/set-state! owner :had-invalid-attempt true))
        ))))

(defn view [app-state owner]
  (reify
    om/IInitState
    (init-state [_]
      {
       :username            ""
       :password            ""
       :had-invalid-attempt false
       })
    om/IWillMount
    (will-mount [_])
    om/IRenderState
    (render-state [this {:keys [had-invalid-attempt] :as local-state}]
      (if-not (:logged-in app-state)
        (dom/div nil
                 (dom/h3 nil "Login")
                 (dom/form #js{:className "col s12"}

                           (dom/div #js{:className "row"}
                                    (dom/div #js{:className "input-field col s6"}
                                             (dom/input #js {
                                                             :type      "text"
                                                             :id        "username"
                                                             :ref       "username"
                                                             :value     (:username local-state)
                                                             :onChange  #(do
                                                                          (util/sync-value-change-to % owner :username)
                                                                          (om/set-state! owner :had-invalid-attempt false))
                                                             :className "validate"})
                                             (dom/label #js {:htmlFor "username"} "username")))
                           (dom/div #js{:className "row"}
                                    (dom/div #js{:className "input-field col s6"}
                                             (dom/input #js {
                                                             :type      "password"
                                                             :id        "password"
                                                             :ref       "password"
                                                             :value     (:password local-state)
                                                             :onChange  #(do
                                                                          (util/sync-value-change-to % owner :password)
                                                                          (om/set-state! owner :had-invalid-attempt false))
                                                             :className "validate"})
                                             (dom/label #js {:htmlFor "password"} "password"))))

                 (dom/button #js {
                                  :onClick   #(onLoginClick app-state owner local-state)
                                  :className "waves-effect waves-light btn"} "Login to Github")
                 (if had-invalid-attempt
                   (dom/div #js{:className "row"}
                            (dom/div #js{:className "input-field col s6"}
                                     (dom/i #js{:className "mdi-alert-error"})
                                     "  Invalid username/password"
                                     )))
                 )))))

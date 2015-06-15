(ns ^:figwheel-always github-board.core.cljs.agents
  (:require
    [om.core :as om :include-macros true]
    [cljs.core.async :as async :refer [chan close!]])
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]))

(defmulti publish :op)
(defmethod publish :on-login
  [{:keys [username password]}]
  (om/update! app-state :username username)
  (om/update! app-state :password password)
  (om/update! app-state :logged-in true))
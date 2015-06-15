(ns ^:figwheel-always github-board.core.cljs.views.login-view
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [github-board.core.cljs.util :as util]
            [cljs.core.async :as async :refer [<!]]
            [github-board.core.cljs.api.github :as github])
  (:require-macros
    [cljs.core.async.macros :refer [go]]))


(defn view [app-state owner]
  (reify
    om/IRender
    (render [this-view])))
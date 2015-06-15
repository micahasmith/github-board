(ns ^:figwheel-always github-board.core.cljs.util
  (:require
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]
    [cljs.core.async :as async :refer [chan close!]])
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]))

(defn sync-value-change-to [e data data-key]
  (let [value (.. e -target -value)]
    ;(.log js/console "set-state!" data data-key value)
    (om/set-state! data data-key value)))
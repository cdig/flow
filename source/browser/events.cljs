;; Wraps calls to the browser Event API

;; Side Effects √
;; State –
;; Deps √

(ns browser.events
  (:require [browser.window :refer [window]]))

(defn addEventListener! [type handler]
  (.addEventListener window type handler))

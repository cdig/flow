;; Wraps calls to the browser Event API

;; Side Effects √
;; State –
;; Deps √

(ns browser.events
  (:require [browser.window :as window]))

(defn addWindowListener! [type handler]
  (window/addEventListener! type handler))

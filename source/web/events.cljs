;; Wraps calls to the browser Event API

;; Side Effects √
;; State –
;; Deps √

(ns web.events
  (:require [web.window :as window]))

(defn addWindowListener! [type handler]
  (window/addEventListener! type handler))

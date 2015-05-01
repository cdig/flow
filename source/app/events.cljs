;; Allows other systems to separately listen for browser events, process the event data, and act accordingly.

(ns app.events
  (:require [browser.events :refer [addWindowListener!]]))

(defonce receiver (atom nil))

;; PUBLIC

(defn setup!
  [r]
  (reset! receiver r))

(defn register-event-handler!
  "Register a handler (function) that will be called whenever a given event-name (string) occurs. Wraps addEventListener on window, so event-name strings should be given accordingly. Handler functionss should take the event data object, and return either nil (to skip the event), or a vector with two elements: a (possibly different) event-name keyword, and a data map containing nicely-processed event data. We will forward the processed event on to the rest of the system."
  [event-name handler]
  (addWindowListener!
    event-name
    (fn [e]
      ; (.preventDefault e) ;; Causes the mouse to flicker
      (.stopPropagation e)
      (when-let [processed (handler e)]
        (@receiver processed)))))

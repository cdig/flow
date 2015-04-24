(ns web.all
  (:require [web.document :as document]
            [web.window :as window]))

(defn setup! [win]
  
  ;; Setup the window first, then other stuff that uses it
  (window/setup! win)
  (document/setup! (.-document win)))

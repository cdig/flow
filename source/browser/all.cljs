(ns browser.all
  (:require [browser.document :as document]
            [browser.window :as window]))

(defn setup! [win]
  
  ;; Setup the window first, then other stuff that uses it
  (window/setup! win)
  (document/setup! (.-document win)))

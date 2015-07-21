;;;; App
;; The app namespace holds most the state, and most of the control flow, in our system.
;; This file is the entry point of the system, and orchestrates everything.

(ns ^:figwheel-always app.app
  (:require [app.engine :as engine]
            [app.render :as render]
            [app.undo :as undo]
            [app.world :as world]
            [browser.all :as browser]
            [debug.testem :as testem]
            [entity.entity :as entity]
            ; [gui.all :as gui]
            [io.events :as events]
            [logic.logics :as logics]
            ))

;; DEBUG

; (prn (world/fetch))

;; HELPERS

(defn- save-state! [w]  w)
(defn- safe-print [v] (print v) v)

;; MAIN

(defn- update-world!
  [new-world]
  (-> new-world
      logics/act
      world/save!
      ))

(defn- render-world!
  "Render the current state of the world to the screen"
  [dT]
  (-> (world/fetch)
      render/act!
      ))

(defn- initialize!
  "In the correct order, set up all the subsystems in our app. Takes the js window object. Return value should be ignored."
  [window]
  (browser/setup! window)
  (-> (world/create)
      (events/setup! world/fetch #(update-world! %)) ;; Anon function makes this more reloadable for figwheel
      ; gui/setup
      testem/setup
      world/save!)
  (undo/save! (entity/all :user (world/fetch)))
  (engine/start! #(render-world! %))) ;; Anon function makes this more reloadable for figwheel

;; PUBLIC

;; This is how we bootstrap the app. In the future, different production environments might require changing this.
(defonce initialized (do (initialize! js/window) true))

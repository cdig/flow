;;;; App
;; The app namespace holds most the state, and most of the control flow, in our system.
;; This file is the entry point of the system, and orchestrates everything.

(ns ^:figwheel-always app.app
  (:require [app.engine :as engine]
            [app.events :as events]
            [app.render :as render]
            [app.undo :as undo]
            [app.world :as world]
            [browser.all :as browser]
            [debug.testem :as testem]
            [entity.entity :as entity]
            [gui.all :as gui]
            [handlers.all :as handlers]
            [logic.logics :as logics]
            [object.object :as object]))

;; HELPERS

(defn- save-state! [w] (undo/save! (object/all w)) w)
(defn- safe-print [w] (print w) w)

;; MAIN

(defn- tick!
  "Update the world by threading it through all subsystems, in the correct order. Takes the delta time since the last tick. Return value should be ignored."
  [dT]
  (-> (world/fetch)
      (assoc :dT dT)
      events/drain!
      logics/act
      render/act!
      ; safe-print
      world/save!))

(defn- initialize!
  "In the correct order, set up all the subsystems in our app. Takes the js window object. Return value should be ignored."
  [window]
  (browser/setup! window)
  (handlers/setup!)
  (-> (world/create)
      entity/setup
      object/setup
      gui/setup
      testem/setup
      save-state!
      world/save!)
  (engine/start! #(tick! %)))

;; PUBLIC

;; This is how we bootstrap the app. In the future, different production environments might require changing this.
(defonce initialized (do (initialize! js/window) true))

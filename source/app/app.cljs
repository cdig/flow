;; One app.cljs to rule them all!

(ns ^:figwheel-always app.app
  (:require [handlers.all]
            [app.db :as db]
            [app.engine :as engine]
            [app.events :as events]
            [app.logics :as logics]
            [app.render :as render]
            [app.testem :as testem]))

(defn- tick! [dT]
  (let [events (events/drain-queue!)]
    (logics/update-world! events)
    (render/render-world!)))

(defn initialize! []
  (engine/start! tick!))
(defonce initialized (do (initialize!) true))

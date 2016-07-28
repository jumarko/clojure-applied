(ns clojure-applied.ch1.image
  (:require [clojure.java.io :as io])
  (:import [javax.imageio ImageIO]
           [java.awt.image BufferedImage]))

(defrecord PlanetImage [src ^BufferedImage contents])

(defn make-planet-image
  "Make a PlanetImage: may throw IOException"
  [src]
  ;; Error:  java.lang.IllegalArgumentException: No matching field found: close for class java.awt.image.BufferedImage
  #_(with-open [img (ImageIO/read (io/input-stream src))]
    (->PlanetImage src img)))

(make-planet-image "resources/jupiter.jpg")

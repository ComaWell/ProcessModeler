ProcessModeler

This project contains in-progress code for computing Samples into a Hidden Markov Model using the K-means learning algorithm. The core of this program is defined in TrackerCore (https://github.com/ComaWell/TrackerCore).

This project uses Jahmm's (https://github.com/KommuSoft/jahmm/tree/v0.1) implentations of Hidden Markov Models and the K-means learner. At present it is still early in the development process; work still needs to be done for converting Samples into vectors that the K-means learner can properly use. The existing (very exploratory) code throws an exception due to the Samples not producing a positive-definite covariance matrix.

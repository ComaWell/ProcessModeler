ProcessModeler

This project contains in-progress code for computing Samples into a Hidden Markov Model using the K-means learning algorithm. The core of this program is defined in TrackerCore (https://github.com/ComaWell/TrackerCore).

This project uses Jahmm's (https://github.com/KommuSoft/jahmm/tree/v0.1) implentations of Hidden Markov Models and the K-means learner. At present it is still early in the development process; work still needs to be done for converting Samples into vectors that the K-means learner can properly use. The existing (very exploratory) code throws an exception due to the Samples not producing a positive-definite covariance matrix.

The project also contains a partially-commented out Main class, the goal of which is to gather Samples for a single process (unlike ProcessTracker, which samples all processes on the machine). The Samples gathered would then be fed into the generated HMM to predict the hidden state of the process. The Main class was in the process of being refactored from its initial, exploratory state to more closely match the Main class of ProcessTracker.

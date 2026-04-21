# CS370 – Current & Emerging Topics in Computer Science
Machine Learning Portfolio

This repository contains projects and assignments completed for **CS370 – Current Emerging Topics in Computer Science**.  
The work in this repository explores concepts in **machine learning, neural networks, and deep learning using Python and Keras**.

The goal of this repository is to document experiments, model implementations, and dataset analysis conducted throughout the course.

---

# Repository Structure

```
CS370/
│
├── README.md
├── notebooks/
├── src/
```

### Folder Descriptions

**notebooks/**  
Jupyter notebooks used for experimentation, training models, and visualizing results.

**src/**  
Python scripts implementing machine learning models and training pipelines.


---

# Technologies Used

The projects in this repository primarily use the following technologies:

- Python
- NumPy
- TensorFlow
- Keras
- Scikit-learn
- Jupyter Notebook

---

# Example Machine Learning Workflow

Typical workflow used in the projects within this repository:

1. Load dataset
2. Preprocess and normalize data
3. Split dataset into training and testing sets
4. Define model architecture
5. Train the model
6. Evaluate performance
7. Analyze results

---

# Cartpole Dataset Structure

```
X_train
Y_train
X_test
Y_test
```

| Variable | Description |
|--------|-------------|
| X_train | Input features used for training |
| Y_train | Labels corresponding to training data |
| X_test | Input features used for evaluation |
| Y_test | Labels for testing data |

Example: MNIST dataset

```
X_train shape: (60000, 28, 28)
Y_train shape: (60000)

X_test shape: (10000, 28, 28)
Y_test shape: (10000)
```

---

# Example Model Architecture

A simple neural network used for digit classification:

```
Input Layer: 784 neurons (28x28 pixels)
Hidden Layer: 128 neurons
Output Layer: 10 neurons (digits 0–9)
```

Activation functions:

- ReLU (hidden layer)
- Softmax (output layer)

Loss function:

- categorical_crossentropy

Optimizer:

- stochastic gradient descent (SGD)

---

# Example Results

Example model performance:

Training Accuracy: 98%  
Testing Accuracy: 97%

---

# License

This repository is intended for **educational purposes** as part of the CS370 coursework.

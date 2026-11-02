const express = require('express');
const router = express.Router();
const { expressjwt: jwt } = require('express-jwt');
const auth = jwt({
    secret: process.env.JWT_SECRET,
    userProperty: 'payload',
    algorithms: ['HS256']
});

const authController = require('../controllers/authentication');
const tripsController = require('../controllers/trips');

// Public authentication routes
router.route('/register').post(authController.register);
router.route('/login').post(authController.login);

// Public read routes
router.route('/trips').get(tripsController.tripsList);
router.route('/trips/:tripCode').get(tripsController.tripsFindByCode);

// Protected write routes requiring JWT bearer authentication
router.route('/trips')
    .post(auth, tripsController.tripsAddTrip);

router.route('/trips/:tripCode')
    .put(auth, tripsController.tripsUpdateTrip);

module.exports = router;

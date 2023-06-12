// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref, onValue } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, signInWithEmailAndPassword, onAuthStateChanged, setPersistence, browserSessionPersistence } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

// Your web app's Firebase configuration
const firebaseConfig = {

  apiKey: "AIzaSyDHsp8a_NU7F8J-Gr5DaaWsoPUN79JO_ZY",

  authDomain: "proiectip-f2b57.firebaseapp.com",

  databaseURL: "https://proiectip-f2b57-default-rtdb.firebaseio.com",

  projectId: "proiectip-f2b57",

  storageBucket: "proiectip-f2b57.appspot.com",

  messagingSenderId: "134775428923",

  appId: "1:134775428923:web:a3542cd019c1e515c4891e",

  measurementId: "G-EGZ06QWFGX"

};

const app = initializeApp(firebaseConfig);

const auth = getAuth();

var signinForm = document.querySelector('#login');
/*
signinForm.addEventListener('click', (e) => {
  persistence: 'session'
  var email = document.getElementById('email').value;
  var password = document.getElementById('password').value;
  e.preventDefault();
  console.log('aici');
  signInWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      // Signed in 
      const user = userCredential.user;

      const database = getDatabase(app);
      const usersRef = ref(database, 'Users/');

      onValue(usersRef, (snapshot) => {
        const users = snapshot.val();
        const currentUser = auth.currentUser;
        const userData = users[currentUser.uid];
        const uid = currentUser.uid;
        var email = user.email;
        console.log(email);
        var userType = userData.userType;
        switch (userType) {
          case 'admin':
            document.location = 'Admin.html?uid=' + uid;
            break;
          case 'medic':
            document.location = 'WelcomeMedic.html?uid=' + uid;
            break;
          case 'pacient':
            document.location = 'WelcomePacient.html?uid=' + uid;
            break;
        }
      });

      alert('Logged in successfully');
      console.log('ceva');
    })
    .catch((error) => {
      const errorCode = error.code;
      const errorMessage = error.message;
      alert(errorMessage);
      console.log('Not good');
    });
});*/
setPersistence(auth, browserSessionPersistence)
  .then(() => {
    console.log('Persistence set successfully.');

    signinForm.addEventListener('click', async (e) => {
      e.preventDefault();
      var email = document.getElementById('email').value;
      var password = document.getElementById('password').value;

      try {
        const userCredential = await signInWithEmailAndPassword(auth, email, password);
        // Signed in successfully
        const user = userCredential.user;
        localStorage.setItem('currentUser', JSON.stringify(user));
        const database = getDatabase(app);
        const usersRef = ref(database, 'Users/');

        onValue(usersRef, (snapshot) => {
          const users = snapshot.val();
          const currentUser = auth.currentUser;
          const userData = users[currentUser.uid];
          const uid = currentUser.uid;
          var email = user.email;
          console.log(email);
          var userType = userData.userType;
          switch (userType) {
            case 'admin':
              document.location = 'Admin.html?uid=' + uid;
              break;
            case 'medic':
              document.location = 'WelcomeMedic.html?uid=' + uid;
              break;
            case 'pacient':
              document.location = 'WelcomePacient.html?uid=' + uid;
              break;
          }
        });
      } catch (error) {
        // Error handling
        const errorCode = error.code;
        const errorMessage = error.message;
        alert(errorMessage);
        console.log('Sign-in error:', errorCode);
      }
    });
  })
  .catch((error) => {
    console.log('Error setting persistence:', error);
  });
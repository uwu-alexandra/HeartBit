function showAuthenticatedElements() {
    const elements = document.querySelectorAll('.authenticated');
    elements.forEach((element) => {
      element.style.display = 'block';
    });
  }
  
  // Helper function to hide authenticated elements
  function hideAuthenticatedElements() {
    const elements = document.querySelectorAll('.authenticated');
    elements.forEach((element) => {
      element.style.display = 'none';
    });
  }
  hideAuthenticatedElements();
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref, onValue } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, signOut, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

const firebaseConfig = {
    // ...
    apiKey: "AIzaSyDHsp8a_NU7F8J-Gr5DaaWsoPUN79JO_ZY",

    authDomain: "proiectip-f2b57.firebaseapp.com",

    databaseURL: "https://proiectip-f2b57-default-rtdb.firebaseio.com",

    projectId: "proiectip-f2b57",

    storageBucket: "proiectip-f2b57.appspot.com",

    messagingSenderId: "134775428923",

    appId: "1:134775428923:web:a3542cd019c1e515c4891e",

    measurementId: "G-EGZ06QWFGX"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
// Get a reference to the database
const database = getDatabase(app);
const usersRef = ref(database, 'Users/');
const auth = getAuth();
onAuthStateChanged(auth, (user) => {
    const urlParams = new URLSearchParams(window.location.search);
    const uidParam = urlParams.get('uid');
  
    if (!user || (uidParam && user.uid !== uidParam)) {
      window.location.href = 'index.html'; // Redirect to index.html if no user is logged in or uid does not match
    } else {
      const userTypeRef = ref(database, `Users/${user.uid}/userType`);
      onValue(userTypeRef, (snapshot) => {
        const userType = snapshot.val();
        if (uidParam && user.uid === uidParam) {
          showAuthenticatedElements();
        } else {
          window.location.href = 'index.html'; // Redirect to index.html if user is not of type 'medic'
        }
      });
    }
  });
let currentUserType;

var logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
    e.preventDefault();
    console.log('in eventlistener');
    signOut(auth).then(() => {
        alert('Signed out successfully');
        document.location = 'index.html';
    }).catch((error) => {
        console.log('Not logged in');
    });
});
const urlParams = new URLSearchParams(window.location.search);
const uid = urlParams.get('uid');
const user = document.getElementById('user');
let currentUser = '';
let userData = '';
let users = '';//let uid = '';
onValue(usersRef, (snapshot) => {
    users = snapshot.val();
    currentUser = auth.currentUser;
    userData = users[currentUser.uid];
    //uid = currentUser.uid;
    console.log(currentUser.uid);
});

onValue(usersRef, (snapshot) => {
    snapshot.forEach((childSnapshot) => {
        if (childSnapshot.key === currentUser.uid) {
            currentUserType = childSnapshot.val().userType;
            console.log(currentUserType);
        }
    });
});

let userType = '';
onValue(usersRef, (snapshot) => {
    snapshot.forEach((childSnapshot) => {
        if (childSnapshot.key === uid) {
            //console.log('usrtype = ' + childSnapshot.val().userType);
            userType = childSnapshot.val().userType;
            let gen = 'x';
            if (childSnapshot.val().cnp.startsWith('1') || childSnapshot.val().cnp.startsWith('5'))
                gen = 'M';
            else
                gen = 'F';
            let an = '';
            if (childSnapshot.val().cnp[0] === '1' || childSnapshot.val().cnp[0] === '2')
                an = '19' + childSnapshot.val().cnp.substring(1, 3);
            else
                an = '20' + childSnapshot.val().cnp.substring(1, 3);
            user.innerHTML = `
            <div class="container">
            <h1 class="info-heading">Nume: ${childSnapshot.val().nume}</h1>
            <h1 class="info-heading">Email: ${childSnapshot.val().email}</h1>
            <h1 class="info-heading">CNP: ${childSnapshot.val().cnp}</h1>
            <h1 class="info-heading">ID: ${childSnapshot.val().id}</h1>
            <h1 class="info-heading">Data nasterii: ${an} - ${childSnapshot.val().cnp.substring(3, 5)} -  ${childSnapshot.val().cnp.substring(5, 7)}</h1>
            <h1 class="info-heading">Gen: ${gen}</h1>
            <h1 class="info-heading">Parola: ${childSnapshot.val().password}</h1>
            <button id="schimba" class="change-password-button">Doresti sa schimbi parola?</button>
          </div>
            `;
            const schimba = document.getElementById('schimba');
            schimba.addEventListener('click', () => {
                window.location.href = `SchimbareParola.html?uid=${uid}`;
            });
            console.log('usrtype = ' + currentUserType);
            const rec = document.getElementById('rec');
            const fisMed = document.getElementById('fisMed');
            const hist = document.getElementById('hist');
            fisMed.addEventListener('click', () => {
                window.location.href = `FisaMedicala.html?uid=${uid}`;
            });
            if (currentUserType === 'pacient') {
                document.getElementById('v5_47033').innerHTML = 'Recomandări';
            }
            else {
                document.getElementById('v5_47033').innerHTML = 'Pacienti';
                document.getElementById('fisMed').innerHTML = '';
                document.getElementById('hist').innerHTML = '';
            }

            rec.addEventListener('click', () => {
                if (currentUserType === 'pacient') {
                    window.location.href = `RecomandarePacient.html?uid=${uid}`;
                }
                else {
                    window.location.href = `Pacienti.html?uid=${uid}`;

                }
            });

            hist.addEventListener('click', () => {
                window.location.href = `Istoric.html?uid=${uid}`;
            });
        }
    });
});

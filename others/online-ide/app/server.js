const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const { execSync } = require('child_process');
const fs = require('fs');
const app = express();
const port = 3000; // İstediğiniz bir port numarası seçebilirsiniz

// CORS politikalarını ayarla
app.use(cors());

// JSON veri alımını etkinleştir
app.use(bodyParser.json());

// POST isteği için /compile endpoint'i oluştur
app.post('/compile', (req, res) => {
    console.log("in function!")
    const language = req.body.language;
    const code = req.body.code;

    const random = Math.random().toString(36).substring(7);
    const filePath = `temp/${random}.${language}`;

    // Kodu dosyaya yazma
    fs.writeFileSync(filePath, code);

    let output = '';

    try {
        switch (language) {
            case "php":
                output = execSync(`C:\\wamp64\\bin\\php\\php5.6.40\\php.exe ${filePath} 2>&1`, { encoding: 'utf-8' });
                break;
            case "python":
                output = execSync(`C:\\Users\\KOUSIK\\AppData\\Local\\Programs\\Python\\Python39\\python.exe ${filePath} 2>&1`, { encoding: 'utf-8' });
                break;
            case "node":
                fs.renameSync(filePath, `${filePath}.js`);
                output = execSync(`node ${filePath}.js 2>&1`, { encoding: 'utf-8' });
                break;
            case "c":
                const outputExe = `${random}`;
                execSync(`gcc ${filePath} -o ${outputExe}`);
                
              output = execSync(`./${outputExe} 2>&1`, { encoding: 'utf-8' }); // "./" ekleyerek çalıştırın
                break;
            case "cpp":
                const outputExeCpp = `${random}.exe`;
                execSync(`g++ ${filePath} -o ${outputExeCpp}`);
                output = execSync(`${__dirname}/${outputExeCpp} 2>&1`, { encoding: 'utf-8' });
                break;
            case "Java":
                const name = random
                const fileName = random
                const outputClass = `${name}.class`;
                execSync(`javac ${filePath} -d ${fileName}`);
                output = execSync(`java ${outputClass} 2>&1`, { encoding: 'utf-8' });
                break;
            default:
                output = "Unsupported language";
        }
    } catch (error) {
        output = error.message;
    }

    // Sonucu istemciye gönder
    res.send(output);
});

// Sunucuyu dinlemeye başla
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});

-- Corrige os hashes BCrypt das contas de demonstração descritas em V2.
UPDATE usuario
SET senha = '$2a$10$ztFAqRsnqN2okUCSP9Gr5OPMKYtYmwnJaxjVLn/Q2jFG5WbjIuV0q'
WHERE email = 'admin@esg.com';

UPDATE usuario
SET senha = '$2a$10$qzw78gQfeodK.TWHv8XzNebEflGWhJIn2UGKTJqWgao9s6Wjum/bm'
WHERE email = 'analista@esg.com';

COMMIT;

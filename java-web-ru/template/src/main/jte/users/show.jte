@import exercise.dto.users.UserPage
@param UserPage page

<html>
    <head>
        <title>Хекслет</title>
    </head>
    <body>
        <h1>${page.getUser().getFirstName()}</h1>
        <h1>${page.getUser().getLastName()}</h1>
        <h1>${page.getUser().getId()}</h1>
        <h1>${page.getUser().getEmail()}</h1>
    </body>
</html>
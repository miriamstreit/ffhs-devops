# Commiting to GitLab

To commit changes to a GitLab repository, you need to follow these steps:

1. **Open Repository**: Navigate to the repository where you want to commit changes.

2. **Clone Repository**: If you haven't already cloned the repository, click on the "Clone" button to copy the repository URL.

<image src="img/gitlab-clone-repo.png" alt="gitlab clone repo" width="250" />

3. **Clone Repository Locally**: Open a terminal and use the `git clone` command to clone the repository to your local machine.

    ```bash
    git clone <repository-url>
    ```

4. **Make Changes**: Make the necessary changes to the files in the repository.

5. **Stage Changes**: Use the `git add` command to stage the changes for commit.

    ```bash
    git add .
    ```

6. **Commit Changes**: Use the `git commit` command to commit the changes with a message.

    ```bash
    git commit -m "Commit message here"
    ```

> Optionally, you can check your staged changes with `git status` before committing.

7. **Push Changes**: Use the `git push` command to push the changes to the remote repository.

    ```bash
    git push
    ```

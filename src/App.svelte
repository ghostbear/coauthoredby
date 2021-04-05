<script>
	import { Octokit } from "@octokit/core";

	let coAuthors = [
		{ username: "ghost" }
	]

	let commitMessage = ""

	function add() {
		coAuthors = coAuthors.concat([{ username: "" }])
	}

	function reset() {
		coAuthors = [{ username: "" }]
	}

	function remove(coAuthor) {
		coAuthors = coAuthors.filter(author => author !== coAuthor)
	}

	function submit() {
		var t0 = performance.now()
		Promise.all(client.getAuthors(coAuthors))
			.then(values => {
				commitMessage = values.map(value => {
					let username = value.data.login
					let id = value.data.id
					return `Co-authored-by: ${username} <${id}+${username}@users.noreply.github.com>`
				}).join("\n")
			}) 
			var t1 = performance.now()
			console.log("Call to doSomething took " + (t1 - t0) + " milliseconds.")e
	}

	function copyToClipboard() {
		navigator.clipboard.writeText(snippet)
	}

	const client = (() => {
		const octokit = new Octokit();
		const authors = new Map()
		return {
			getAuthors: (list) => {
				return list.map(author => {
					console.log(authors)
					return (authors[author.username] = authors[author.username] ?? octokit.request('GET /users/{username}', {username: author.username}))
				})
			}
		}
	})()
</script>

<main>
	<h1>Co-authored</h1>
	<p>Simple tool to give credit to users who help you with a commit on GitHub</p>
	<br>
	<b>What is the username of the co-author(s)?</b>
	<div class="co-authors">
		{#each coAuthors as coAuthor}
			<div class="co-author">
				<input placeholder="username" bind:value={coAuthor.username}>
				<button on:click={remove(coAuthor)}>
					Remove
				</button>
			</div>
		{/each}
	</div>

	<div>
		<button on:click={add}>
			Add new
		</button>
		<button on:click={reset}>
			Reset
		</button>
		<button on:click={submit}>
			Submit
		</button>
	</div>

	{#if commitMessage}
		<code id="co-author" on:click={copyToClipboard} contenteditable="true" bind:innerHTML={commitMessage}></code>
	{/if}
</main>

<style>
	main {
		text-align: center;
		padding: 1em;
		max-width: 240px;
		margin: 0 auto;
	}

	h1 {
		color: #ff3e00;
		text-transform: uppercase;
		font-size: 4em;
		font-weight: 100;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>
import React from 'react';
import { Button, Grid, Paper } from '@mui/material';
import DropdownButton from './DropdownButton';
import { sendJson } from '../backend_conn';
import initValues from '../StaticValues';
import useStyles from '../Styles';

export default function DatacatPanel1(props) {
    const classes = useStyles();
    const repository = props.repository;
    const triple = {
        subject: "",
        predicate: "",
        object: "",
    }

    const [elValue, setElValue] = React.useState(initValues);
    function handleCallback(id, item) {
        const newElValue = elValue.map((i, eid) => eid === id ? item : i);
        setElValue(newElValue);
    }

    function sendTriples(triple1) {
        const response = async () => {
            const res = await sendJson("POST", "/insertclassdefinition/repo/" + repository, triple1);
            console.log(res);
        }
        response();
    }

    return (
        <div className={classes.maxWidth}>
            <Grid container spacing={1} columns={8} className={classes.root}>
                <Grid item sm={2} >
                    <Paper className={classes.text}>
                        Semantische Annotation von Geometrieelementen mit Ontologie aus Datacat
                    </Paper>
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={3} elValue={elValue[3]} initValue="" repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <Paper className={classes.text} style={{ lineHeight: "50px" }}>
                        rdf:type
                    </Paper>
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={0} elValue={elValue[0]} initValue="" repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={1} elValue={elValue[1]} initValue={elValue[0].value} repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={2} elValue={elValue[2]} initValue={elValue[1].value} repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <Button className={classes.button} onClick={() => {
                        triple.instance = elValue[3].value;
                        triple.className = elValue[2].value;
                        sendTriples(triple);
                    }}>Save</Button>
                </Grid>
            </Grid>
        </div>
    )
}